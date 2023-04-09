package hours22.daedeokserver.naver.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hours22.daedeokserver.exception.ErrorCode;
import hours22.daedeokserver.exception.business.BusinessException;
import hours22.daedeokserver.naver.dto.NaverDTO;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@Service
public class NaverService {

    @Value("${naver.secretKey}")
    private String secretKey;
    @Value("${naver.accessKey}")
    private String accessKey;
    @Value("${naver.serviceKey}")
    private String serviceKey;
    @Value("${naver.phoneNumber}")
    private String phoneNumber;

    public void sendSms(String phoneNum, String content) throws JsonProcessingException, UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, URISyntaxException {
        Long time = System.currentTimeMillis();
        List<NaverDTO.Message> messageList = new ArrayList<>();
        messageList.add(new NaverDTO.Message(phoneNum, content));

        NaverDTO naverDTO = new NaverDTO("SMS", "COMM", "82", phoneNumber, "대덕바이블 SMS", messageList);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonBody = objectMapper.writeValueAsString(naverDTO);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-ncp-apigw-timestamp", String.valueOf(time));
        headers.set("x-ncp-iam-access-key", accessKey);

        String sig = makeSignature(time);
        headers.set("x-ncp-apigw-signature-v2", sig);

        HttpEntity<String> body = new HttpEntity<>(jsonBody, headers);

        RestTemplate restTemplate = new RestTemplate();
        NaverDTO.Response response = restTemplate.postForObject(new URI("https://sens.apigw.ntruss.com/sms/v2/services/" + serviceKey + "/messages"), body, NaverDTO.Response.class);

        if (!response.getStatusCode().equals("202"))
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    private String makeSignature(Long time) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {

        String space = " ";                    // one space
        String newLine = "\n";                    // new line
        String method = "POST";                    // method
        String url = "/sms/v2/services/" + serviceKey + "/messages";    // url (include query string)
        String timestamp = String.valueOf(time);            // current timestamp (epoch)

        String message = new StringBuilder()
                .append(method)
                .append(space)
                .append(url)
                .append(newLine)
                .append(timestamp)
                .append(newLine)
                .append(accessKey)
                .toString();

        SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes("UTF-8"), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(signingKey);

        byte[] rawHmac = mac.doFinal(message.getBytes("UTF-8"));
        String encodeBase64String = Base64.encodeBase64String(rawHmac);

        return encodeBase64String;
    }
}
