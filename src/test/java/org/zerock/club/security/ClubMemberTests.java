package org.zerock.club.security;

import java.util.Optional;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.zerock.club.entity.ClubMember;
import org.zerock.club.entity.ClubMemberRole;
import org.zerock.club.repository.ClubMemberRepository;

@SpringBootTest
public class ClubMemberTests {
    
    @Autowired
    private ClubMemberRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void insertDummies(){

        IntStream.rangeClosed(1, 100).forEach(i -> {
            ClubMember clobMember = ClubMember.builder()
                                              .email("user"+i+"@zerock.org")
                                              .name("사용자"+i)
                                              .fromSocial(false)
                                              .password(  passwordEncoder.encode("1111") )
                                              .build();
            clobMember.addMemberRole(ClubMemberRole.USER);

            if (i >80) {
                clobMember.addMemberRole(ClubMemberRole.MANAGER);
            }
            if (i >90) {
                clobMember.addMemberRole(ClubMemberRole.ADMIN);
            }
            repository.save(clobMember);
        });
    }

    @Test
    public void testRead(){
        Optional<ClubMember> result = repository.findByEmail("user95@zerock.org", false);

        ClubMember clubMember = result.get();

        System.out.println(clubMember);
    }
}
