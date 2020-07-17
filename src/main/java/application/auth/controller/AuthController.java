package application.auth.controller;

import application.auth.models.About;
import application.auth.services.AboutService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value = "Auth API")
@RequestMapping("/")
public class AuthController {

    private static Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AboutService aboutService;

    /**
     * @return about auth
     */
    @ApiOperation(value = "About Auth")
    @GetMapping(path = "/about", produces = "application/json")
    @ResponseBody
    public About aboutAuth() {
        return aboutService.getInfo();
    }

    /**
     * Handle auth header
     *
     * @return HTTP 200 if success
     */
    @ApiOperation(value = "Get authentication api")
    @RequestMapping(value = "/authenticate", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<?> getAuthenticate() {
        logger.debug("GET /authenticate");

        return ResponseEntity.ok().build();
    }

    /**
     * Handle auth header
     *
     * @return HTTP 200 if success
     */
    @ApiOperation(value = "Create a new authentication token given username/password")
    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<?> postAuthenticate() {
        logger.debug("POST /authenticate");

        return ResponseEntity.ok().build();
    }

}
