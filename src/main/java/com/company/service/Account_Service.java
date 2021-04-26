package com.company.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.company.dto.Account_dto;

public interface Account_Service extends UserDetailsService{

	public List<Account_dto> findAll();

	public void delete(Integer[] id);

	public Account_dto save(Account_dto account_dto);

	public Account_dto findOneById(Integer id);

	public List<Account_dto> findByIdRole(Integer idRole);

	Account_dto findOneByName(String name);

}
