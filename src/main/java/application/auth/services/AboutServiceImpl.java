package application.auth.services;

import org.springframework.stereotype.Service;

import application.auth.models.About;

@Service
public class AboutServiceImpl implements AboutService{

	@Override
	public About getInfo() {
		return new About("Auth Service", "Storefront", "Authorization and Authentication");
	}

}
