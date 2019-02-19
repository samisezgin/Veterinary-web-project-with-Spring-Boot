package com.example.demo.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.model.Customer;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.CustomerRepository;

@Controller
@RequestMapping("/customers")
public class CustomerController {
	@Autowired
	CustomerRepository customerRepository;

	
	@RequestMapping(value="/get-all-customers",method = RequestMethod.GET)
	public String getAllCustomers(Map<String, Object> map) {
		Authentication auth  = SecurityContextHolder.getContext().getAuthentication();
		List<Customer> customers=customerRepository.findAll();
    	map.put("adminname", auth.getName());  
    	map.put("customers", customers);
		return "customer/get-all-customers";
	}
	
	
	@RequestMapping(value="/customer-insert-panel",method = RequestMethod.GET)
	public String CustomerRegisterPanel(Map<String, Object> map) {
		Authentication auth  = SecurityContextHolder.getContext().getAuthentication();
    	map.put("adminname", auth.getName());  
    	map.put("customer", new Customer());
		return "customer/customer-insert-panel";
	}
	
    @RequestMapping(value = "/customer-insert-panel", method = RequestMethod.POST)
    public String saveRegisterPage(@Valid @ModelAttribute("user") Customer customer, BindingResult result, Model model,Map<String, Object> map) {
    	
        model.addAttribute("user", customer);
        if (result.hasErrors()) {
            return "customer-insert-panel";
        } else {
        	customerRepository.save(customer);
    		Authentication auth  = SecurityContextHolder.getContext().getAuthentication();
        	map.put("adminname", auth.getName());  
        	map.put("customer", new Customer());
    		map.put("message", "Successful");

        }
        return "customer-insert-panel";
    }
    
	@RequestMapping(value="/show-customer/{customerid}",method = RequestMethod.GET)
	public String CustomerShowPanel(@PathVariable int customerid, Map<String, Object> map) {
		Customer customer=customerRepository.findById(customerid).get();
		Authentication auth  = SecurityContextHolder.getContext().getAuthentication();
    	map.put("adminname", auth.getName());  
    	map.put("customer", customer);
		return "customer/show-customer";
	}
    
    
	@RequestMapping(value="/delete-customer/{customerid}",method = RequestMethod.GET)
	public String CustomerDelete(@PathVariable int customerid, Map<String, Object> map) {
		Customer customer=customerRepository.findById(customerid).get();
		customerRepository.delete(customer);
		
		Authentication auth  = SecurityContextHolder.getContext().getAuthentication();
		List<Customer> customers=customerRepository.findAll();
    	map.put("adminname", auth.getName());  
    	map.put("customers", customers);
		map.put("message", "Successful");
		return "customer/get-all-customers";
	}

}
