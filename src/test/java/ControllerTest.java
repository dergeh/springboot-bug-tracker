import bugTracker.controller.TicketController;
import bugTracker.model.Ticket;
import bugTracker.service.TicketService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ControllerTest {

    @InjectMocks
    TicketController controller;

    @Mock
    TicketService service;

    @Test
    public void createTicketTest(){
        Ticket testTicket= new Ticket("test","test ticket", true);
        Ticket emptyTicket= new Ticket(null,null,false);
        when(service.create(any())).thenReturn(testTicket);

        //normal case
        ResponseEntity<Ticket> testResponse=controller.createTicket(testTicket);
        assertEquals(HttpStatus.CREATED,testResponse.getStatusCode());
        assertEquals( "test",testResponse.getBody().getId());
        assertEquals(true, testResponse.getBody().isOpen());
        assertEquals("test ticket", testResponse.getBody().getDescription());

        //empty body
        ResponseEntity<Ticket> testFailResponse=controller.createTicket(emptyTicket);
        assertEquals(HttpStatus.BAD_REQUEST,testFailResponse.getStatusCode());
        assertEquals(null,testFailResponse.getBody());

        verify(service, times(1)).create(testTicket);

    }

    @Test
    public void createNoIdTest(){
        Ticket testTicket= new Ticket(null,"test ticket", true);
        when(service.create(any())).thenReturn(testTicket);

        ResponseEntity<Ticket> testResponse=controller.createTicket(testTicket);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,testResponse.getStatusCode());
        assertEquals( null,testResponse.getBody());

        verify(service, times(1)).create(testTicket);


    }

    @Test
    public void getTicketTest(){
        Ticket testTicket= new Ticket("test","test ticket", true);
        when(service.get(eq("test"))).thenReturn(testTicket);

        //ticket exists
        ResponseEntity<Ticket> testResponse=controller.getTicket("test");
        assertEquals(HttpStatus.OK,testResponse.getStatusCode());
        assertEquals( "test",testResponse.getBody().getId());
        assertEquals(true, testResponse.getBody().isOpen());
        assertEquals("test ticket", testResponse.getBody().getDescription());

        //ticket doesnt exist
        ResponseEntity<Ticket> testResponseNotFound=controller.getTicket("no");
        assertEquals(HttpStatus.NOT_FOUND, testResponseNotFound.getStatusCode());

        verify(service, times(2)).get(anyString());
    }

    @Test
    public void updateTicketTest(){
        Ticket testTicket= new Ticket("test","test ticket", true);
        Ticket updatedTicket= new Ticket("test","test ticket updated", false);
        when(service.get(eq("test"))).thenReturn(testTicket);
        when(service.update(anyString(), any())).thenReturn(updatedTicket);

        // success
        ResponseEntity<Ticket> testResponseSuccess = controller.updateTicket(updatedTicket,"test");
        assertEquals(HttpStatus.OK,testResponseSuccess.getStatusCode());
        assertEquals("test", testResponseSuccess.getBody().getId());
        assertEquals("test ticket updated", testResponseSuccess.getBody().getDescription());
        assertEquals(false,testResponseSuccess.getBody().isOpen());

        // failure due to no ticket to update
        ResponseEntity<Ticket> testResponseFail = controller.updateTicket(updatedTicket,"fail");
        assertEquals(HttpStatus.NOT_FOUND,testResponseFail.getStatusCode());
        assertNull(testResponseFail.getBody());

        verify(service, times(2)).get(anyString());
        verify(service,times(1)).update(anyString(),any());
    }
}
