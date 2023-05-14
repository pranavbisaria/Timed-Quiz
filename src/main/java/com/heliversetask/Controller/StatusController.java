package com.heliversetask.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StatusController {
    @Operation(summary = "This is developer information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Server Working Successfully", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/")
    public String test(){
        return "index.html";
    }
}
