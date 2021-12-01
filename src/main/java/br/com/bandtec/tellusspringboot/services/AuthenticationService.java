package br.com.bandtec.tellusspringboot.services;

import br.com.bandtec.tellusspringboot.domains.Gerente;
import br.com.bandtec.tellusspringboot.domains.Login;
import br.com.bandtec.tellusspringboot.domains.Responsavel;
import br.com.bandtec.tellusspringboot.repositories.GerenteRepository;
import br.com.bandtec.tellusspringboot.repositories.ResponsavelRepository;
import com.amazonaws.services.acmpca.model.InvalidArgsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService implements UserDetailsService {

    @Autowired
    private ResponsavelRepository respRepository;

    @Autowired
    private GerenteRepository gerRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Gerente> ger = gerRepository.findGerenteByEmail(email);
        Optional<Responsavel> resp = respRepository.findResponsavelByEmail(email);
        if(resp.isPresent()){
            Optional<Responsavel> optional = respRepository.findResponsavelByEmail(email);

            if(optional.isPresent()) {
                BCryptPasswordEncoder passwordEnconder = new BCryptPasswordEncoder();
                optional.get().setSenha(passwordEnconder.encode(optional.get().getSenha()));
                return new Login(optional.get().getEmail(), optional.get().getSenha());
            }

            throw new UsernameNotFoundException("Responsavel not found");
        } else if(ger.isPresent()){
            Optional<Gerente> optional = gerRepository.findGerenteByEmail(email);

            if(optional.isPresent()) {
                BCryptPasswordEncoder passwordEnconder = new BCryptPasswordEncoder();
                optional.get().setSenha(passwordEnconder.encode(optional.get().getSenha()));
                return new Login(optional.get().getEmail(), optional.get().getSenha());
            }

            throw new UsernameNotFoundException("Gerente not found");
        }

        throw new InvalidArgsException("Email não encontrado no banco");
    }
}