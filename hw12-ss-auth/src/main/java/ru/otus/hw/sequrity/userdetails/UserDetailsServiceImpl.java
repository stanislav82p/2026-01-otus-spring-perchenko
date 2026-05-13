package ru.otus.hw.sequrity.userdetails;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.otus.hw.models.entity.ReaderEntity;
import ru.otus.hw.repositories.ReaderRepository;

import java.util.Optional;

@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final ReaderRepository readerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<ReaderEntity> optReader = readerRepository.findById(username);
        if (optReader.isEmpty()) {
            throw new UsernameNotFoundException("User is not found for username: " + username);
        } else {
            return new AppUserDetails(optReader.get());
        }
    }
}
