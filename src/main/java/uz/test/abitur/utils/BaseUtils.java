package uz.test.abitur.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class BaseUtils {
    private final Random random;

    public String generateCode() {
        return String.valueOf(random.nextInt(100_000, 999_999));
    }

    public static String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

    public static String generateFileName() {
        return UUID.randomUUID().toString();
    }
}
