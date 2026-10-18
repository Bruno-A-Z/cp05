package com.java.cp04.Amazin.config;

import com.java.cp04.Amazin.enumerator.Role;
import com.java.cp04.Amazin.enumerator.Sector;
import com.java.cp04.Amazin.enumerator.Size;
import com.java.cp04.Amazin.model.Item;
import com.java.cp04.Amazin.model.Usuario;
import com.java.cp04.Amazin.repository.ItemRepository;
import com.java.cp04.Amazin.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final ItemRepository itemRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.security.admin.username}")
    private String adminUsername;

    @Value("${app.security.admin.password}")
    private String adminPassword;

    @Override
    public void run(String... args) {
        if (!usuarioRepository.existsByUsername(adminUsername)) {
            Usuario admin = new Usuario();
            admin.setNome("Administrador");
            admin.setUsername(adminUsername);
            admin.setEmail(adminUsername + "@mercadoexpress.com");
            admin.setSenha(passwordEncoder.encode(adminPassword));
            admin.setRole(Role.ADMIN);
            usuarioRepository.save(admin);
        }

        if (itemRepository.count() == 0) {
            itemRepository.saveAll(List.of(
                    new Item(null, "Sabão líquido", "Limpeza", Sector.CLEANING, Size.MEDIUM, 12.90),
                    new Item(null, "Meias esportivas", "Vestuário", Sector.SOCKS, Size.MEDIUM, 19.90),
                    new Item(null, "Detergente", "Limpeza", Sector.CLEANING, Size.BIG, 3.99)
            ));
        }
    }
}
