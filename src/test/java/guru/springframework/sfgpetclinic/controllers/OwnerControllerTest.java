package guru.springframework.sfgpetclinic.controllers;

import guru.springframework.sfgpetclinic.fauxspring.BindingResult;
import guru.springframework.sfgpetclinic.fauxspring.Model;
import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.services.OwnerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OwnerControllerTest {

    private static final String OWNERS_CREATE_OR_UPDATE_OWNER_FORM = "owners/createOrUpdateOwnerForm";
    private static final String REDIRECT_OWNERS_5 = "redirect:/owners/5";

    @Mock(lenient = true)
    OwnerService ownerService;

    @InjectMocks
    OwnerController controller;

    @Mock
    BindingResult bindingResult;

    @Mock
    Model model;

    @Captor
    ArgumentCaptor<String> stringArgmentCaptor;

    @BeforeEach
    void setup() {
        given(ownerService.findAllByLastNameLike(stringArgmentCaptor.capture()))
                .willAnswer(invocation -> {
            List<Owner> owners = new ArrayList<>();
            String name = invocation.getArgument(0);
            if (name.equals("%Buck%")) {
                owners.add(new Owner(1l, "Joe", "Buck"));
                return owners;
            } else if (name.equals("%DontFindMe%")) {
                return owners;
            } else if (name.equals("%FindMe%")) {
                owners.add(new Owner(1L, "Joe", "Buck"));
                owners.add(new Owner(2L, "Joe", "Buck"));
                return owners;
            }
            throw new RuntimeException("Invali Argumnet");
        });
    }

    @Test
    void processFindForWildcardFound() {
        //given
        Owner owner = new Owner(1l, "Joe", "FindMe");
        InOrder inOrder = inOrder(ownerService, model);
        //when
        String viewName = controller.processFindForm(owner, bindingResult, model);
        //then
        assertThat("%FindMe%").isEqualToIgnoringCase(stringArgmentCaptor.getValue());
        assertThat("owners/ownersList").isEqualToIgnoringCase(viewName);
        // inorder asserts
        inOrder.verify(ownerService).findAllByLastNameLike(anyString());
        inOrder.verify(model).addAttribute(anyString(), anyList());
        verifyNoMoreInteractions(model);
    }

    @Test
    void processFindForWildcardNotFound() {
        //given
        Owner owner = new Owner(1l, "Joe", "DontFindMe");
        //when
        String viewName = controller.processFindForm(owner, bindingResult, model);
        //then
        assertThat("%DontFindMe%").isEqualToIgnoringCase(stringArgmentCaptor.getValue());
        assertThat("owners/findOwners").isEqualToIgnoringCase(viewName);
        verifyNoMoreInteractions(model);
    }

/*
    @Test
    void processFindFormWildcardString() {
        //given
        Owner owner = new Owner(1l, "Joe", "Buck");
        List<Owner> ownerList = new ArrayList<>();
        final ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        /**
         * List<Owner> results = ownerService.findAllByLastNameLike("%"+ owner.getLastName() + "%");
         * の呼び出し時の引数を検証するためのgiven
         *
        given(ownerService.findAllByLastNameLike(captor.capture())).willReturn(ownerList);
        //when
        String viewName = controller.processFindForm(owner, bindingResult, null);
        //then
        assertThat("%Buck%").isEqualToIgnoringCase(captor.getValue());
    }
*/
    @Test
    void processFindFormWildcardStringAnnotation() {
        //given
        Owner owner = new Owner(1l, "Joe", "Buck");
        List<Owner> ownerList = new ArrayList<>();
        /**
         * List<Owner> results = ownerService.findAllByLastNameLike("%"+ owner.getLastName() + "%");
         * の呼び出し時の引数を検証するためのgiven
         * */
        //BeforeEachにて実装済みのためコメントアウト
        //given(ownerService.findAllByLastNameLike(stringArgmentCaptor.capture())).willReturn(ownerList);
        //when
        String viewName = controller.processFindForm(owner, bindingResult, null);
        //then
        assertThat("%Buck%").isEqualToIgnoringCase(stringArgmentCaptor.getValue());
        assertThat("redirect:/owners/1").isEqualToIgnoringCase(viewName);
        verifyNoMoreInteractions(model);
    }

    @Test
    void processCreationFormHasErrors() {
        //given
        Owner owner = new Owner(1l, "Joe", "Buck");
        given(bindingResult.hasErrors()).willReturn(true);
        //given(controller.processCreationForm(owner, bindingResult)).willReturn(returnValue);

        //when
        String viewName = controller.processCreationForm(owner, bindingResult);

        //then
        assertThat(viewName).isEqualToIgnoringCase(OWNERS_CREATE_OR_UPDATE_OWNER_FORM);
        verifyZeroInteractions(model);
    }

    @Test
    void processCreationFormNoErrors() {
        //given
        Owner owner = new Owner(5l, "Joe", "Buck");
        given(bindingResult.hasErrors()).willReturn(false);
        given(ownerService.save(any())).willReturn(owner);

        //when
        String viewName = controller.processCreationForm(owner, bindingResult);

        //then
        assertThat(viewName).isEqualToIgnoringCase(REDIRECT_OWNERS_5);
    }
}