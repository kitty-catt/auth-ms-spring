package application.auth.services;

import application.auth.models.About;
import org.springframework.stereotype.Service;

@Service
public class AboutServiceImpl implements AboutService {

    @Override
    public About getInfo() {
        return new About("Auth Service", "Storefront", "Authorization and Authentication");
    }

}
