package FlagAssessment.Prabhav.service;

import FlagAssessment.Prabhav.DTO.FlagRequest;
import FlagAssessment.Prabhav.Exception.DuplicateFlagException;
import FlagAssessment.Prabhav.entity.Flag;
import FlagAssessment.Prabhav.repository.FlagRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FlagServiceTest {

    @Mock
    private FlagRepository repository;

    @InjectMocks
    private FlagServiceImpl service;

    @Test
    void shouldCreateFlag() {

        FlagRequest request = new FlagRequest();
        request.setName("NEW_UI");
        request.setEnabled(true);
        request.setRolloutPercentage(25);
        request.setTargetedUsers(Set.of("john", "alice"));
        request.setDefaultValue(false);

        Flag saved = Flag.builder()
                .id(1L)
                .tenantId("tenant1")
                .name("NEW_UI")
                .enabled(true)
                .rolloutPercentage(25)
                .targetedUsers(Set.of("john", "alice"))
                .defaultValue(false)
                .version(0L)
                .build();

        when(repository.existsByTenantIdAndName("tenant1", "NEW_UI"))
                .thenReturn(false);

        when(repository.save(any(Flag.class)))
                .thenReturn(saved);

        var response = service.create("tenant1", request);

        assertEquals("NEW_UI", response.getName());
        assertEquals(true, response.isEnabled());
        assertEquals(25, response.getRolloutPercentage());
        assertEquals(Set.of("john", "alice"), response.getTargetedUsers());
        assertEquals(false, response.isDefaultValue());
        assertEquals(0L, response.getVersion());

        verify(repository).save(any(Flag.class));
    }

    @Test
    void shouldDeleteFlag() {

        Flag flag = Flag.builder()
                .id(1L)
                .tenantId("tenant1")
                .name("NEW_UI")
                .enabled(true)
                .rolloutPercentage(25)
                .targetedUsers(Set.of("john"))
                .defaultValue(false)
                .version(0L)
                .build();

        when(repository.findByIdAndTenantId(1L, "tenant1"))
                .thenReturn(Optional.of(flag));

        service.delete(1L, "tenant1");

        verify(repository).delete(flag);
    }

    @Test
    void shouldThrowExceptionWhenFlagAlreadyExists() {

        FlagRequest request = new FlagRequest();
        request.setName("NEW_UI");

        when(repository.existsByTenantIdAndName("tenant1", "NEW_UI"))
                .thenReturn(true);

        assertThrows(DuplicateFlagException.class,
                () -> service.create("tenant1", request)
        );

        verify(repository, never()).save(any());
    }

    @Test
    void shouldUpdateFlag() {

        FlagRequest request = new FlagRequest();
        request.setName("NEW_UI");
        request.setEnabled(true);
        request.setRolloutPercentage(50);
        request.setTargetedUsers(Set.of("john", "alice"));
        request.setDefaultValue(false);
        request.setVersion(0L);

        Flag existing = Flag.builder()
                .id(1L)
                .tenantId("tenant1")
                .name("NEW_UI")
                .enabled(true)
                .rolloutPercentage(25)
                .targetedUsers(Set.of("john"))
                .defaultValue(false)
                .version(0L)
                .build();

        Flag updated = Flag.builder()
                .id(1L)
                .tenantId("tenant1")
                .name("NEW_UI")
                .enabled(true)
                .rolloutPercentage(50)
                .targetedUsers(Set.of("john", "alice"))
                .defaultValue(false)
                .version(1L)
                .build();

        when(repository.findByIdAndTenantId(1L, "tenant1"))
                .thenReturn(Optional.of(existing));

        when(repository.save(any(Flag.class)))
                .thenReturn(updated);

        var response = service.update(1L, "tenant1", request);

        assertEquals(50, response.getRolloutPercentage());
        assertEquals(Set.of("john", "alice"), response.getTargetedUsers());

        verify(repository).save(any(Flag.class));
    }
}