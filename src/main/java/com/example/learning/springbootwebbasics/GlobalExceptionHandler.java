package com.example.learning.springbootwebbasics;

import java.net.URI;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice // @ControllerAdvice + @ResponseBody を含む。REST API 向け。
public class GlobalExceptionHandler {

    // バリデーションエラー（MethodArgumentNotValidException）のハンドリング
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {

        // RFC 7807 に準拠した ProblemDetail オブジェクトを作成
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.BAD_REQUEST, // 400 Bad Request
            "Validation failed to request body." // エラーの詳細メッセージ
        );

        problemDetail.setTitle("Bad Request"); // エラーのタイトル
        problemDetail.setType(URI.create("about:blank")); // 問題タイプの URI (RFC 7870)
        problemDetail.setInstance(URI.create(request.getDescription(false).substring(4))); // リクエスト URI

        // エラーフィールドとメッセージのマップを作成
        Map<String, String> errors = ex.getBindingResult().getFieldErrors().stream()
            .collect(Collectors.toMap(
                fieldError -> fieldError.getField(), // エラーが発生したフィールド名
                fieldError -> fieldError.getDefaultMessage(), // デフォルトのエラーメッセージ
                (oldValue, newValue) -> oldValue, // キーが重複した場合の解決策
                LinkedHashMap::new // 順序を保存するマップ
            ));
        
        // errors マップを ProblemDetail の properties に追加
        problemDetail.setProperty("errors", errors);
        problemDetail.setProperty("timestamp", Instant.now()); // 発生日時

        // ProblemDetail をレスポンスボディとして返す
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }

    // その他の一般的な例外のハンドリング
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleGeneralException(Exception ex, WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.INTERNAL_SERVER_ERROR, // 500 Internal Server Error
            "An unexpected error occured." // 一般的なエラーメッセージ
        );

        problemDetail.setTitle("Internal Server Error");
        problemDetail.setType(URI.create("about:blank"));
        problemDetail.setInstance(URI.create(request.getDescription(false).substring(4)));
        problemDetail.setProperty("timestamp", Instant.now());

        // 開発環境などでデバッグ用にスタックトレースを含めることも可能だが
        // 本番環境ではセキュリティのため含めないのが一般的。
        // problemDetail.setProperty("statckTrace", Arrays.toString(ex.getStackTrace()));

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problemDetail);
    }

}
