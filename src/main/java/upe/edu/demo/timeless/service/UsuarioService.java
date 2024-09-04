package upe.edu.demo.timeless.service;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import upe.edu.demo.timeless.model.Usuario;
import upe.edu.demo.timeless.repository.UsuarioRepository;

@Service
@AllArgsConstructor
public class UsuarioService {


    private final UsuarioRepository usuarioRepository;




    public Usuario findByUsername(String username){
        return usuarioRepository.findByUsername(username).orElseThrow(()->new RuntimeException("Usuario no encontrado"))    ;
    }

    public Usuario save(Usuario usuario){
        return usuarioRepository.save(usuario);
    }

    public Usuario findById(Integer id){
        return usuarioRepository.findById(id).orElseThrow(()->new RuntimeException("Usuario no encontrado"));
    }

    public void deleteById(Integer id){
        usuarioRepository.deleteById(id);
    }

    public Iterable<Usuario> findAll(){
        return usuarioRepository.findAll();
    }

    public boolean existsById(Integer id){
        return usuarioRepository.existsById(id);
    }

}
