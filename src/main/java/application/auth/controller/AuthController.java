package application.auth.controller;

import application.auth.CustomerAuthenticationProvider;
import application.auth.models.About;
import application.auth.models.Customer;
import application.auth.services.AboutService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@Api(value = "Auth API")
@RequestMapping("/")
public class AuthController {

    private static Logger logger = LoggerFactory.getLogger(AuthController.class);
    @Value("${customerService.url}")
    private String custResourceUrl;
    @Autowired
    private CustomerAuthenticationProvider customerAuthenticationProvider;

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

    /**
     * Handle auth header
     *
     * @return HTTP 200 if success
     */
    @ApiOperation(value = "Create a new authentication token given username/password")
    @RequestMapping(value = "/customer", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<?> getCustomerDetails() {
        logger.debug("GET /customer");

        Authentication authentication = customerAuthenticationProvider.getAuthentication();
        String name = authentication.getName();
        String password = authentication.getCredentials().toString();

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Customer> responseEntity = restTemplate.getForEntity(custResourceUrl + "?username={name}", Customer.class, name);
        Customer customer_array = responseEntity.getBody();

        logger.debug("customer service returned:" + customer_array);

        if (customer_array == null) {
            throw new AuthenticationException("Invalid username or password") {
                private static final long serialVersionUID = 1L;
            };
        }

        final Customer cust = customer_array;

        if (!cust.getPassword().equals(password)) {
            throw new AuthenticationException("Invalid username or password") {
                private static final long serialVersionUID = 1L;
            };
        }
        System.out.println("CUSTOMER " + cust.toString());

        return ResponseEntity.ok(customer_array);
    }

}
