package com.heliversetask.Controller;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class StatusController implements ErrorController {
    @Operation(summary = "This is developer information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Server Working Successfully", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/")
    public String test(){
        return "index.html";
    }

    //Handle Error Pages (But hidden from the documentation)
    @Hidden
    @RequestMapping(value = "/error")
    @ResponseBody
    public Map<String, Object> error() {
        Map<String, Object> errorAttributes = new HashMap<>();
        errorAttributes.put("message", "The requested resource is not available. [Error 404 NOT FOUND]");
        errorAttributes.put("success", false);
        return errorAttributes;
    }
}
