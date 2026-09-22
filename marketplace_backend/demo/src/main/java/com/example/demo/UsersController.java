package com.example.demo;

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
    public ResponseEntity<?> loginUser(@RequestBody Map <String, String> body)
    {
        String email  = body.get("email");
        String password  = body.get("password");
        if(email == null || password == null || email.trim().isEmpty() || password.trim().isEmpty())
        {
            return ResponseEntity.badRequest().body("{\"message\": \"Email и пароль обязательны для заполнения!\"}");
        }

        Optional<Users> userOpt = usersRepository.findAll().stream()
        .filter(u -> u.getEmail() != null && u.getEmail().equalsIgnoreCase(email.trim()))
        .findFirst();
        if(userOpt.isPresent())
        {
            
            Users user = userOpt.get();
            
            if(user.getPassword() != null && user.getPassword().equals(password.trim()))
            {
                user.setPassword(null);
                return  ResponseEntity.ok(user);
            }
        }
        return ResponseEntity.status(401).body("{\"message\": \"Неверный email или пароль\"}");
    }

    @PostMapping("/users/{id}/become_vendor")
    public ResponseEntity<?> becomeVendor(@PathVariable Long id, @RequestBody Map<String, String> body)
    {
        String shopName = body.get("shopName");
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
            Users updatedUser = usersRepository.save(user);
            updatedUser.setPassword(null);
            return ResponseEntity.ok(updatedUser);
        }
        return  ResponseEntity.notFound().build();
    }

    @PostMapping ("/user/register")
    public ResponseEntity<?> registerUser(@RequestBody Map<String, String> body)
    {
        String name = body.get("user_name");
        String email = body.get("email");
        String password = body.get("password");

        if (name == null || name.trim().isEmpty() || 
            email == null || email.trim().isEmpty() || 
            password == null || password.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("{\"message\": \"Все поля обязательны для заполнения!\"}");
        }
        Optional<Users> existingUser = usersRepository.findAll().stream()
        .filter(u -> u.getUserName() != null && u.getUserName().equalsIgnoreCase(name.trim()))
        .findFirst();

        if (existingUser.isPresent()) {
            return ResponseEntity.badRequest().body("{\"message\": \"Пользователь с таким именем уже существует!\"}");
        }
        Optional<Users> existingEmail = usersRepository.findAll().stream()
        .filter(u -> u.getEmail() != null && u.getEmail().equalsIgnoreCase(email.trim()))
        .findFirst();

        if (existingEmail.isPresent()) {
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
}