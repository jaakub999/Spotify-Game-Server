package com.spotify.game.service.impl;

import com.spotify.game.exception.ExceptionCode;
import com.spotify.game.exception.SgRuntimeException;
import com.spotify.game.helper.EmailType;
import com.spotify.game.model.entity.User;
import com.spotify.game.model.entity.VerificationToken;
import com.spotify.game.repository.UserRepository;
import com.spotify.game.repository.VerificationRepository;
import com.spotify.game.service.VerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static com.spotify.game.helper.EmailType.REGISTER;

@Service
public class VerificationServiceImpl implements VerificationService {

    private final VerificationRepository verificationRepository;
    private final UserRepository userRepository;

    @Autowired
    public VerificationServiceImpl(VerificationRepository verificationRepository, UserRepository userRepository) {
        this.verificationRepository = verificationRepository;
        this.userRepository = userRepository;
    }

    @Override
    public VerificationToken generateVerificationToken(String email) {
        String token = UUID.randomUUID().toString();
        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(15);
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setEmail(email);
        verificationToken.setExpirationTime(expirationTime);

        verificationRepository.save(verificationToken);

        return verificationToken;
    }

    @Override
    @Transactional
    public void verifyToken(String token, EmailType emailType) {
        Optional<VerificationToken> verificationToken = Optional.ofNullable(verificationRepository.findByToken(token)
                .orElseThrow(() -> new SgRuntimeException(ExceptionCode.E005)));

        if (verificationToken.isPresent()) {
            VerificationToken tokenEntity = verificationToken.get();
            checkToken(tokenEntity);
            User user = userRepository.findByEmail(tokenEntity.getEmail())
                    .orElseThrow(() -> new SgRuntimeException(ExceptionCode.E001));

            if (emailType == REGISTER) {
                if (user.isVerified())
                    throw new SgRuntimeException(ExceptionCode.E007);

                user.setVerified(true);
                userRepository.save(user);
                verificationRepository.delete(tokenEntity);
            }
        }

        else throw new SgRuntimeException(ExceptionCode.E005);
    }

    @Override
    public String getEmailByToken(String token) {
        Optional<VerificationToken> verificationToken = verificationRepository.findByToken(token);

        if (verificationToken.isPresent()) {
            VerificationToken tokenEntity = verificationToken.get();
            checkToken(tokenEntity);
            String email = tokenEntity.getEmail();
            verificationRepository.delete(tokenEntity);

            return email;
        }

        else throw new SgRuntimeException(ExceptionCode.E005);
    }

    private void checkToken(VerificationToken token) {
        if (token.getExpirationTime().isBefore(LocalDateTime.now())) {
            verificationRepository.delete(token);
            throw new SgRuntimeException(ExceptionCode.E006);
        }
    }
}
