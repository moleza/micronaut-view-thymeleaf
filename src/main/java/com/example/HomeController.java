package com.example;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Error;
import io.micronaut.http.annotation.Get;
import io.micronaut.views.ModelAndView;
import io.micronaut.views.View;
import io.micronaut.views.exceptions.ViewRenderingException;
import ognl.MethodFailedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.exceptions.TemplateProcessingException;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;

@Controller("/")
public class HomeController {

    private static final Logger log = LoggerFactory.getLogger(HomeController.class);

    @View("home")
    @Get("/")
    public Map<String, Object> index() {
        var date = LocalDateTime.now().toString().substring(0,19).replace("T","");
        return Map.of("dateTime", date);
    }

    @Get("/test")
    public HttpResponse<?> test() {
        try {
            return HttpResponse.ok(new ModelAndView<>("fragment", Collections.singletonMap("user", new User("Name", "name@test.com"))));
        } catch (Exception e) {
            return HttpResponse.serverError("Template Failed to Load!");
        }
    }

    private record User(String name, String email) {}

    @Error(exception = TemplateProcessingException.class, global = true)
    public HttpResponse<String> handleTemplateError(TemplateProcessingException exception) {
        log.error("Template processing error: {}", exception.getMessage());
        return HttpResponse.serverError("Internal Server Error"); // Or a custom error view
    }

    @Error(exception = ognl.MethodFailedException.class, global = true)
    public HttpResponse<String> handleTemplateError(MethodFailedException exception) {
        log.error("Template processing error: {}", exception.getMessage());
        return HttpResponse.serverError("Internal Server Error"); // Or a custom error view
    }

    @Error(exception = NoSuchMethodException.class, global = true)
    public HttpResponse<String> handleTemplateError(NoSuchMethodException exception) {
        log.error("Template processing error: {}", exception.getMessage());
        return HttpResponse.serverError("Internal Server Error"); // Or a custom error view
    }

    @Error(exception = ognl.NoSuchPropertyException.class, global = true)
    public HttpResponse<String> handleTemplateError(ognl.NoSuchPropertyException exception) {
        log.error("Template processing error: {}", exception.getMessage());
        return HttpResponse.serverError("Internal Server Error"); // Or a custom error view
    }

    @Error(exception = ViewRenderingException.class, global = true)
    public HttpResponse<String> handleTemplateError(ViewRenderingException exception) {
        log.error("Template processing error: {}", exception.getMessage());
        return HttpResponse.serverError("Internal Server Error"); // Or a custom error view
    }
}
