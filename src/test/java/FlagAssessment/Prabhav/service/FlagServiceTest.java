package FlagAssessment.Prabhav.service;

import FlagAssessment.Prabhav.DTO.FlagRequest;
import FlagAssessment.Prabhav.entity.Flag;
import FlagAssessment.Prabhav.repository.FlagRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
        request.setDefaultValue(false);

        Flag saved = Flag.builder()
                .id(1L)
                .tenantId("tenant1")
                .name("NEW_UI")
                .enabled(true)
                .defaultValue(false)
                .build();

        when(repository.existsByTenantIdAndName("tenant1","NEW_UI"))
                .thenReturn(false);

        when(repository.save(any()))
                .thenReturn(saved);

        assertEquals(
                "NEW_UI",
                service.create("tenant1",request).getName()
        );

        verify(repository).save(any());
    }

    @Test
    void shouldDeleteFlag(){

        Flag flag = Flag.builder()
                .id(1L)
                .tenantId("tenant1")
                .name("NEW_UI")
                .enabled(true)
                .defaultValue(false)
                .build();

        when(repository.findByIdAndTenantId(1L,"tenant1"))
                .thenReturn(Optional.of(flag));

        service.delete(1L,"tenant1");

        verify(repository).delete(flag);
    }

}