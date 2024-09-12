package upe.edu.demo.timeless;

import lombok.AllArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import upe.edu.demo.timeless.service.UsuarioService;

@SpringBootApplication
//	@EnableWebMvc
@AllArgsConstructor
@EnableScheduling
public class TimeLessApplication {

	private final UsuarioService usuarioService;



/*	@Scheduled(fixedRate = 5000)
	public void CrearUsuario() {
		usuarioService.CrearUsuario();
	}*/


	public static void main(String[] args) {
		SpringApplication.run(TimeLessApplication.class, args);

	}

}
