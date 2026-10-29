package com.example.demo.controller;


public class ChatRequest
{
    public String message;
    public String userId;

    public String getMessage(){return message;}
    public void setMessage(String message){this.message = message;}

    public String getUserId(){return  userId;}
    public void setUserId(String userId) {this.userId = userId;}
}
