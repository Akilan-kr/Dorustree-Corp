package com.dorustree.dorustree_corp.Service.Interfaces;

import org.springframework.security.core.Authentication;

public interface IAuthenticationFacade {
    Authentication getAuthentication();
}
