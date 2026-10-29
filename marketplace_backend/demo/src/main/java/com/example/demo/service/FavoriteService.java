package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.dto.FavoriteDTO;
import com.example.demo.repository.FavoriteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
@Service 
public class FavoriteService
{
    @Autowired
    private FavoriteRepository favoriteRepository;

    public Page<FavoriteDTO> getFavoritePage(Long userId, int page, int size)
    {
        return favoriteRepository.findByUserId(userId, PageRequest.of(page, size))
        .map(favorite -> new FavoriteDTO(favorite));
    }
}
