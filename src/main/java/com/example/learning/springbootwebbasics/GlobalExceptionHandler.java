package com.example.learning.springbootwebbasics;

import java.net.URI;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.NoHandlerFoundException;

/*
 * アプリケーション全発生する例外をハンドリングするクラスです。
 * @ReestControllerAdvice 全てのコントローラーに対するアドバイスとして機能する
 */
@RestControllerAdvice // @ControllerAdvice + @ResponseBody を含む。REST API 向け。
public class GlobalExceptionHandler {

    /*
     * バリデーションエラー（ jakarta.validation アノテーションによる検証失敗）をハンドリングする。
     * HTTP ステータスコード：400 Bad Request
     * 問題詳細：RFC 7870 （ Problem Details for HTTP APIs ）に基づいた JSON 形式で、
     * どのフィールドでどのようなバリデーションエラーが発生したかの詳細を含む。
     * @Param ex MethodArgumentNotValidException 例外オブジェクト
     * @Param request WebRequest オブジェクト
     * @return カスタムされた ProblemDetail を含む ResponseEntity
     */
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

    /*
     * ハンドラーが見つからない（存在しないパスへのアクセス）例外をハンドリングする。
     * HTTP ステータスコード：404 Not Found
     * 問題詳細：存在しないリソースへのアクセスを通知し、リクエストされたパスを含む。
     * @Param: NoHandlerFoundException 例外オブジェクト
     * @Param: request WebRequest オブジェクト
     * @return: カスタムされた ProblemDetail を含む ResponseEntity
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ProblemDetail> handleNoHandlerFoundException(NoHandlerFoundException ex, WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND, // 404 Not Found
                "The requested resource was not found."
        );
        problemDetail.setTitle("Resource Not Found");
        problemDetail.setType(URI.create("about:blank"));
        problemDetail.setInstance(URI.create(request.getDescription(false).substring(4)));
        problemDetail.setProperty("timesamp", Instant.now());
        problemDetail.setProperty("requestedPath", ex.getRequestURL()); // リクエストされたパスを含める
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problemDetail);
    }

    /*
     * HTTP メソッドがサポートされていない（例：POST エンドポイントに GET でアクセス）例外をハンドリングする。
     * HTTP ステータスコード：405 Method Not Allowed
     * 問題詳細：許可されていない HTTP メソッドを通知し、許可されているメソッドのリストを含む。
     * @Param: ex HttpRequestMethodNotSupportedException 例外オブジェクト
     * @Param: request WebRequest オブジェクト
     * @return: カスタムされた ProblemDetail を含む ResponseEntity
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ProblemDetail> handleMethodNotSupportedException(HttpRequestMethodNotSupportedException ex, WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.METHOD_NOT_ALLOWED, // 405 Method Not Allowed
                "HTTP method '" + ex.getMethod() + "'not supported for this endpoint."
        );
        problemDetail.setTitle("Method Not Allowed");
        problemDetail.setType(URI.create("about:blank"));
        problemDetail.setInstance(URI.create(request.getDescription(false).substring(4)));
        problemDetail.setProperty("timestamp", Instant.now());
        // サポートされている HTTP メソッドをカンマ区切り文字列で含める
        // getSuportedHttpMethods() が null を返す可能性があるため、Optional でラップして安全に処理
        Set<HttpMethod> supportedMethods = ex.getSupportedHttpMethods();
        String methodsString = Optional.ofNullable(supportedMethods)
                .orElse(java.util.Collections.emptySet()) // null の場合は空の Set を返す
                .stream()
                .map(HttpMethod::name) // HttpMethod::name を使用しても、Optional で null チェックしているため安全
                .collect(Collectors.joining(", "));
        problemDetail.setProperty("suportedMethods", methodsString);

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(problemDetail);

    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ProblemDetail> handleResponseStatusException(ResponseStatusException ex, WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            ex.getStatusCode(), // ResponseStatusException が持つ HTTP ステータスコードを使用
            ex.getReason() // ResponseStatusException が持つ理由（メッセージ）を使用
        );
        HttpStatusCode statusCode = ex.getStatusCode();
        if (statusCode instanceof HttpStatusCode) {
            problemDetail.setTitle(((HttpStatus) statusCode).getReasonPhrase());
        } else {
            problemDetail.setTitle(String.valueOf(statusCode.value()));
        }
        problemDetail.setType(URI.create("about:blank"));
        problemDetail.setInstance(URI.create(request.getDescription(false).substring(4)));
        problemDetail.setProperty("timestamp", Instant.now());

        return ResponseEntity.status(ex.getStatusCode()).body(problemDetail);
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
