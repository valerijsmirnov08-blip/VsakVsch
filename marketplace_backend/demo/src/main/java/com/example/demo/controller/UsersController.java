package com.example.demo.controller;
import com.example.demo.entity.Users;
import com.example.demo.repository.UsersRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.Optional;

@RestController 
@RequestMapping ("/api")
@CrossOrigin (origins = "http://localhost:5173")
public class UsersController
{
    @Autowired 
    private UsersRepository usersRepository;   
    @PostMapping("/users/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> body)
    {
        String email = body.get("email");
        String password = body.get("password");

        if (email == null || password == null || email.trim().isEmpty() || password.trim().isEmpty())
        {
            return ResponseEntity.badRequest().body("{\"message\": \"Email и пароль обязательны для заполнения!\"}");
        }
        Optional<Users> userOpt = usersRepository.findByEmailIgnoreCase(email.trim());
        if (userOpt.isPresent())
        {
            Users user = userOpt.get();

            if (user.getPassword() != null && user.getPassword().equals(password.trim()))
            {
                user.setPassword(null);
                return ResponseEntity.ok(user);
            }
        }
        return ResponseEntity.status(401).body("{\"message\": \"Неверный email или пароль\"}");
    }

     @PostMapping ("/user/register")
    public ResponseEntity<?> registerUser(@RequestBody Map<String, String> body)
    {
        String name = body.get("user_name");
        String email = body.get("email");
        String password = body.get("password");

        if (name == null || name.trim().isEmpty() || 
            email == null || email.trim().isEmpty() || 
            password == null || password.trim().isEmpty())
        {
            return ResponseEntity.badRequest().body("{\"message\": \"Все поля обязательны для заполнения!\"}");
        }
        if (usersRepository.existsByUserNameIgnoreCase(name.trim())) {
            return ResponseEntity.badRequest().body("{\"message\": \"Пользователь с таким именем уже существует!\"}");
        }
        if (usersRepository.existsByEmailIgnoreCase(email.trim())) {
            return ResponseEntity.badRequest().body("{\"message\": \"Пользователь с таким Email уже существует!\"}");
        }
        Users newUser = new Users();
        newUser.setUserName(name.trim());
        newUser.setEmail(email.trim());
        newUser.setPassword(password);
        newUser.setIsVendor(false);

        try {
            Users savedUser = usersRepository.save(newUser);
            savedUser.setPassword(null);
            return ResponseEntity.ok(savedUser);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("{\"message\": \"Ошибка сохранения в базу данных: " + e.getMessage() + "\"}");
        }
    }

    @PostMapping("/users/{id}/become_vendor")
    public ResponseEntity<?> becomeVendor(@PathVariable Long id, @RequestBody Map<String, String> body)
    {
        String shopName = body.get("shopName");
        String shopDescription = body.get("shopDescription");
        String brandName = body.get("brandName");
        if(shopName == null || shopName.trim().isEmpty())
        {
            return ResponseEntity.badRequest().body("{\"message\": \"Название магазина не может быть пустым!\"}");
        }
        Optional<Users> userOpt = usersRepository.findById(id);
        if(userOpt.isPresent())
        {
            Users user = userOpt.get();
            user.setIsVendor(true);
            user.setShopName(shopName);
            user.setShopDescription(shopDescription);
            user.setBrandName(brandName);
            Users updatedUser = usersRepository.save(user);
            updatedUser.setPassword(null);
            return ResponseEntity.ok(updatedUser);
        }
        return  ResponseEntity.notFound().build();
    }
    @PostMapping("/user/{userId}")
    public  ResponseEntity<?> updateUserProfile(@PathVariable  Long userId, @RequestBody Map<String, String> body)
    {
        String userName = body.get("userName");
        String shopName = body.get("shopName");
        String shopDescription = body.get("shopDescription");
       
        
        Optional<Users> userOpt = usersRepository.findById(userId);
         
        if(userOpt.isPresent())
        {
            Users user = userOpt.get();
            user.setUserName(userName);
           
            if(user.getIsVendor() != null && user.getIsVendor())
            {
               
                if(shopName == null || shopName.trim().isEmpty())
                {
                    return ResponseEntity.badRequest().body("{\"message\": \"Название магазина не может быть пустым!\"}");
                }
                user.setShopName(shopName);
                user.setShopDescription(shopDescription);
            }
            Users updateProfile = usersRepository.save(user);
            return ResponseEntity.ok(updateProfile);
        }
        return ResponseEntity.notFound().build();
    }
    @PostMapping("/users/{id}/stop_vendor")
    public ResponseEntity<?> stopVendor(@PathVariable Long id) {
        Optional<Users> userOpt = usersRepository.findById(id);
        if (userOpt.isPresent()) {
            Users user = userOpt.get();
            user.setIsVendor(false);
            user.setShopName(null);
            user.setShopDescription(null);
            
            Users updatedUser = usersRepository.save(user);
            updatedUser.setPassword(null);
            return ResponseEntity.ok(updatedUser);
        }
        return ResponseEntity.notFound().build();
    }
   
}