package guru.springframework.sfgpetclinic.services.springdatajpa;

import guru.springframework.sfgpetclinic.model.Speciality;
import guru.springframework.sfgpetclinic.repositories.SpecialtyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SpecialitySDJpaServiceTest {

    @Mock
    SpecialtyRepository specialtyRepository;

    @InjectMocks
    SpecialitySDJpaService service;

    @Test
    void testDeleteByObject() {
        // given
        Speciality speciality = new Speciality();

        // when
        service.delete(speciality);

        // then
        verify(specialtyRepository).delete(any(Speciality.class));
        then(specialtyRepository).should().delete(any(Speciality.class));
    }

    @Test
    void findByIdTest() {
        Speciality speciality = new Speciality();

        when(specialtyRepository.findById(1L)).thenReturn(Optional.of(speciality));

        Speciality foundSpecialty = service.findById(1L);

        assertThat(foundSpecialty).isNotNull();

        verify(specialtyRepository).findById(anyLong());

    }

    @Test
    void findByIdBddTest() {
        Speciality speciality = new Speciality();

        // given
        given(specialtyRepository.findById(1L)).willReturn(Optional.of(speciality));

        // when
        Speciality foundSpeciality = service.findById(1L);

        // then
        assertThat(foundSpeciality).isNotNull();
        //verify(specialtyRepository).findById(anyLong());
        then(specialtyRepository).should().findById(anyLong());
        then(specialtyRepository).should(times(1)).findById(anyLong());
        then(specialtyRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void deleteById() {
        // given - none

        // when
        service.deleteById(1l);
        service.deleteById(2l);

        // then
        //verify(specialtyRepository, times(1)).deleteById(1l);
        then(specialtyRepository).should(times(2)).deleteById(anyLong());
    }

    @Test
    void deleteByIdAtLeast() {
        // given - none

        // when
        service.deleteById(1l);
        service.deleteById(1l);

        // then
        //verify(specialtyRepository, atLeastOnce()).deleteById(1l);
        then(specialtyRepository).should(times(2)).deleteById(anyLong());
    }

    @Test
    void deleteByIdAtMost() {
        //when
        service.deleteById(1l);
        service.deleteById(1l);

        //then
        //verify(specialtyRepository, atMost(5)).deleteById(1l);
        then(specialtyRepository).should(atMost(5)).deleteById(anyLong());
    }

    @Test
    void deleteByIdNever() {
        // when
        service.deleteById(1l);
        service.deleteById(1l);

        // then
        //verify(specialtyRepository, atLeastOnce()).deleteById(1l);
        //verify(specialtyRepository, never()).deleteById(5L);
        then(specialtyRepository).should(atLeastOnce()).deleteById(anyLong());
        then(specialtyRepository).should(never()).deleteById(5l);
    }

    @Test
    void testDelete() {
        // when
        service.delete(new Speciality());
        // then
        then(specialtyRepository).should().delete(any(Speciality.class));
    }
}