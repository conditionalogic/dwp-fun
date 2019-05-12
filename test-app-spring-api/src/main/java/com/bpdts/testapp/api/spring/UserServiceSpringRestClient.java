package com.bpdts.testapp.api.spring;

import com.bpdts.testapp.client.api.spring.UsersApi;
import com.bpdts.testapp.client.invoker.spring.ApiClient;
import com.bpdts.testapp.client.model.spring.User;
import com.bpdts.testapp.client.model.spring.UserData;
import com.bpdts.testapp.client.model.spring.UserDataList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceSpringRestClient
{
    private final UsersApi usersApi;

    @Autowired
    public UserServiceSpringRestClient(RestTemplateBuilder restTemplateBuilder ) {
        RestTemplate restTemplate = restTemplateBuilder.build();
        ApiClient apiClient = new ApiClient( restTemplate );
        this.usersApi = new UsersApi( apiClient );
    }

    public Optional<User> getUser( String id ) {
        try {
            return Optional.ofNullable( usersApi.getUser( id ) );
        }   catch( HttpClientErrorException.NotFound e ) {
            return Optional.empty();
        }
    }

    public UserDataList getUserDataByCity( String city ) {
        return usersApi.listUsersByCity( city );
    }

    public List<UserData> getAllUserData() {
        return usersApi.listAllUsers();
    }
}
