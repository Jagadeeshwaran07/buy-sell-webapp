package com.labweek.menumate.controller;

import com.labweek.menumate.dto.UserDto;
import com.labweek.menumate.entity.UserEntity;
import com.labweek.menumate.services.EmailService;
import com.labweek.menumate.services.ProductService;
import com.labweek.menumate.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.labweek.menumate.entity.NewProductEntity;
import java.util.Optional;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ProductService productService;
 // Assuming you have a ProductEntity class

    @PostMapping("/send-interest")
    public String sendInterestNotification(@RequestParam String ntId, @RequestParam Long prodId) {
        Optional<UserEntity> productOwner = userService.getUserByNtId(ntId);
        Optional<NewProductEntity> product = productService.gettProductById(prodId); // Retrieve product details

        if (productOwner.isPresent() && product.isPresent()) {
            String productOwnerEmail = productOwner.get().getEmail();
            String subject = "Interest in Your Product";

            // Fetch product details
            String productName = product.get().getProductName();

            // Retrieve logged-in user details from SecurityContext
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            String loggedInUserEmail = ((UserDto) auth.getPrincipal()).getEmail(); // Cast to UserDto
            String loggedInUserName = ((UserDto) auth.getPrincipal()).getUserName(); // Username of the logged-in user
            // Construct the email message
            String message = String.format(
                    "Hello,\n\n"
                            + "We’re excited to let you know that someone is interested in your product: %s.\n\n"
                            + "Thank you for using Comcast Buy & Sell to share your listing! Here are the details of the interested buyer:\n\n"
                            + "Buyer Username: %s\n"
                            + "Buyer Email: %s\n\n"
                            + "We hope this connection brings you closer to a successful sale. If you have any questions or need assistance, feel free to reach out to our support team.\n\n"
                            + "Best regards,\n"
                            + "The Comcast Buy & Sell Team"
                    , productName, loggedInUserName, loggedInUserEmail);

            // Send the email
            emailService.sendEmail(productOwnerEmail, subject, message);
            return "Email sent successfully!";
        } else {
            return "Product owner or product not found with provided ntId or productId.";
        }
    }
    }
