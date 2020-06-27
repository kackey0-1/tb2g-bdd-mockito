package guru.springframework.sfgpetclinic.controllers;

import guru.springframework.sfgpetclinic.fauxspring.BindingResult;
import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.services.OwnerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class OwnerControllerTest {

    private static final String OWNERS_CREATE_OR_UPDATE_OWNER_FORM = "owners/createOrUpdateOwnerForm";
    private static final String REDIRECT_OWNERS_5 = "redirect:/owners/5";

    @Mock
    OwnerService ownerService;

    @InjectMocks
    OwnerController controller;

    @Mock
    BindingResult bindingResult;

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
            }
            throw new RuntimeException("Invali Argumnet");
        });
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
        assertThat("asdf").isEqualToIgnoringCase(viewName);
    }

    @Test
    void processCreationFormHasErrors() {
        //given
        Owner owner = new Owner(1l, "Kentaro", "Kakimoto");
        given(bindingResult.hasErrors()).willReturn(true);
        //given(controller.processCreationForm(owner, bindingResult)).willReturn(returnValue);

        //when
        String viewName = controller.processCreationForm(owner, bindingResult);

        //then
        assertThat(viewName).isEqualToIgnoringCase(OWNERS_CREATE_OR_UPDATE_OWNER_FORM);
    }

    @Test
    void processCreationFormNoErrors() {
        //given
        Owner owner = new Owner(5l, "Kentaro", "Kakimoto");
        given(bindingResult.hasErrors()).willReturn(false);
        given(ownerService.save(any())).willReturn(owner);

        //when
        String viewName = controller.processCreationForm(owner, bindingResult);

        //then
        assertThat(viewName).isEqualToIgnoringCase(REDIRECT_OWNERS_5);
    }
}