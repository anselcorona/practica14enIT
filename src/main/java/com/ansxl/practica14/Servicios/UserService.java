package com.ansxl.practica14.Servicios;

import com.ansxl.practica14.Modelos.User;
import com.ansxl.practica14.Repositorios.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;


@Service
@Transactional
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User createUser(Integer id, String name, String email, String password) throws Exception{
        try {
            return userRepository.save(new User(id,name,email,password));
        }catch (Exception ex){
            throw new Exception("Error al guardar usuario");
        }
    }

    public void editUser(User user) throws Exception{
        try {
            userRepository.save(user);
        }catch (Exception ex){
            throw new Exception("Error al editar usuario");
        }
    }

    public boolean userExists(String email, String password){
        User user = userRepository.findByEmailAndPassword(email,password);
        return (user!=null);
    }

    public List<User> userList() {
        return userRepository.findAll();
    }

    public List<User> userListPagination(int offset, int limit){
        return userRepository.pagination(offset,limit);
    }

    @Transactional
    public Integer countUsers() {
        return userRepository.cantidad()+1;
    }

    @Transactional
    public void deleteUser(Integer userId){
        userRepository.deleteById(userId);
    }

    @Transactional
    public User getUser(Integer userID){
        return userRepository.getOne(userID);
    }
}
