package ru.itis.fisd.semestrovka.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class EmailContentBuilder {

    private final TemplateEngine templateEngine;

    public String buildPurchaseEmail(String username, String title, String address, int price) {
        Context context = new Context();
        context.setVariable("username", username);
        context.setVariable("title", title);
        context.setVariable("address", address);
        context.setVariable("price", price);

        return templateEngine.process("email/purchase_confirmation", context);
    }
}
