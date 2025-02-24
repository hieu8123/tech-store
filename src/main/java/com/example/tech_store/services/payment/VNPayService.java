package com.example.tech_store.services.payment;

import com.example.tech_store.config.VNPayConfig;
import com.example.tech_store.enums.PaymentMethod;
import com.example.tech_store.enums.PaymentStatus;
import com.example.tech_store.model.Order;
import com.example.tech_store.model.Payment;
import com.example.tech_store.repository.PaymentRepository;
import com.example.tech_store.utils.VNPayUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class VNPayService implements PaymentService {

    private final VNPayConfig vnPayConfig;
    private final PaymentRepository paymentRepository;

    @Override
    public PaymentMethod getPaymentMethod() {
        return PaymentMethod.VNPAY;
    }

    @Override
    public String processPayment(Order order) {
        String txnRef = generateTxnRef();

        // VNPay yêu cầu số tiền tính theo VND * 100
        long amount = order.getTotal() * 100L;

        Map<String, String> vnpParams = new HashMap<>();
        vnpParams.put("vnp_Version", vnPayConfig.getVersion());
        vnpParams.put("vnp_Command", "pay");
        vnpParams.put("vnp_TmnCode", vnPayConfig.getTmnCode());
        vnpParams.put("vnp_Amount", String.valueOf(amount));
        vnpParams.put("vnp_CurrCode", vnPayConfig.getCurrencyCode());
        vnpParams.put("vnp_TxnRef", txnRef);
        vnpParams.put("vnp_OrderInfo", "Thanh toan don hang " + order.getId().toString().replace("-",""));
        vnpParams.put("vnp_OrderType", "other");
        vnpParams.put("vnp_Locale", vnPayConfig.getLocale());
        vnpParams.put("vnp_ReturnUrl", vnPayConfig.getReturnUrl());
        vnpParams.put("vnp_IpAddr", "127.0.0.1");

        // Thiết lập thời gian tạo và thời gian hết hạn giao dịch
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        String createDate = formatter.format(calendar.getTime());
        vnpParams.put("vnp_CreateDate", createDate);

        calendar.add(Calendar.MINUTE, 15);
        String expireDate = formatter.format(calendar.getTime());
        vnpParams.put("vnp_ExpireDate", expireDate);
        // Sắp xếp tham số theo thứ tự A-Z và tạo chuỗi hashData, query string
        List<String> fieldNames = new ArrayList<>(vnpParams.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();

        for (int i = 0; i < fieldNames.size(); i++) {
            String fieldName = fieldNames.get(i);
            String fieldValue = vnpParams.get(fieldName);
            if (fieldValue != null && !fieldValue.isEmpty()) {
                hashData.append(fieldName)
                        .append("=")
                        .append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8));
                query.append(URLEncoder.encode(fieldName, StandardCharsets.UTF_8))
                        .append("=")
                        .append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8));
                if (i < fieldNames.size() - 1) {
                    hashData.append("&");
                    query.append("&");
                }
            }
        }


        // Tính secure hash sử dụng HMAC SHA512 từ VNPayUtil
        String secureHash = VNPayUtil.hmacSHA512(vnPayConfig.getHashSecret(), hashData.toString());
        query.append("&vnp_SecureHash=").append(secureHash);
        // Xây dựng URL thanh toán
        String paymentUrl = vnPayConfig.getBaseUrl() + "?" + query.toString();
        // Lưu thông tin giao dịch vào database
        Payment payment = Payment.builder()
                .order(order)
                .method(PaymentMethod.VNPAY)
                .status(PaymentStatus.PROCESSING)
                .transactionId(txnRef)
                .build();
        paymentRepository.save(payment);

        return paymentUrl;
    }

    public int handleCallback(HttpServletRequest request) {
        Map<String, String> fields = new HashMap<>();
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            String paramValue = URLEncoder.encode(request.getParameter(paramName), StandardCharsets.US_ASCII);
            if (paramValue != null && !paramValue.isEmpty()) {
                fields.put(paramName, paramValue);
            }
        }

        // Lấy secure hash và loại bỏ các tham số liên quan trước khi tính lại chữ ký
        String vnpSecureHash = request.getParameter("vnp_SecureHash");
        fields.remove("vnp_SecureHash");
        fields.remove("vnp_SecureHashType");

        String signValue = VNPayUtil.hashAllFields(fields, vnPayConfig.getHashSecret());
        if (!signValue.equals(vnpSecureHash)) {
            return -1;
        }

        // Tra cứu giao dịch theo transactionId (vnp_TxnRef)
        String txnRef = request.getParameter("vnp_TxnRef");
        Payment payment = paymentRepository.findByTransactionId(txnRef);
        if (payment == null) {
            return -1;
        }

        // Cập nhật trạng thái giao dịch dựa trên vnp_TransactionStatus
        String transactionStatus = request.getParameter("vnp_TransactionStatus");
        if ("00".equals(transactionStatus)) {
            payment.setStatus(PaymentStatus.PAID);
            paymentRepository.save(payment);
            return 1;
        } else {
            payment.setStatus(PaymentStatus.FAILED);
            paymentRepository.save(payment);
            return 0;
        }
    }

    private String generateTxnRef() {
        long timestamp = System.currentTimeMillis(); // 13 chữ số
        int randomDigits = new Random().nextInt(900) + 100; // 3 chữ số từ 100 đến 999
        return String.valueOf(timestamp) + randomDigits;
    }
}
