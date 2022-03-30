import bugTracker.controller.TicketController;
import bugTracker.model.Ticket;
import bugTracker.service.TicketService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ControllerTest {

    private final Ticket testTicket = new Ticket("test", "test ticket", true);

    @InjectMocks
    TicketController controller;

    @Mock
    TicketService service;

    @Test
    public void createTicketTest() {
        Ticket emptyTicket = new Ticket(null, null, false);
        when(service.create(any())).thenReturn(testTicket);

        //normal case
        ResponseEntity<Ticket> testResponse = controller.createTicket(testTicket);
        assertEquals(HttpStatus.CREATED, testResponse.getStatusCode());
        assertEquals("test", testResponse.getBody().getId());
        assertTrue(testResponse.getBody().isOpen());
        assertEquals("test ticket", testResponse.getBody().getDescription());

        //empty body
        ResponseEntity<Ticket> testFailResponse = controller.createTicket(emptyTicket);
        assertEquals(HttpStatus.BAD_REQUEST, testFailResponse.getStatusCode());
        assertNull(testFailResponse.getBody());

        verify(service, times(1)).create(testTicket);

    }

    @Test
    public void createNoIdTest() {
        Ticket testNoIdTicket = new Ticket(null, "test ticket", true);
        when(service.create(any())).thenReturn(testNoIdTicket);

        ResponseEntity<Ticket> testResponse = controller.createTicket(testNoIdTicket);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, testResponse.getStatusCode());
        assertNull(testResponse.getBody());

        verify(service, times(1)).create(testNoIdTicket);


    }

    @Test
    public void getTicketByIdTest() {
        when(service.findById(eq("test"))).thenReturn(testTicket);

        //ticket exists
        ResponseEntity<Ticket> testResponse = controller.getTicketById("test");
        assertEquals(HttpStatus.OK, testResponse.getStatusCode());
        assertEquals("test", testResponse.getBody().getId());
        assertTrue(testResponse.getBody().isOpen());
        assertEquals("test ticket", testResponse.getBody().getDescription());

        //ticket doesn't exist
        ResponseEntity<Ticket> testResponseNotFound = controller.getTicketById("no");
        assertEquals(HttpStatus.NOT_FOUND, testResponseNotFound.getStatusCode());

        verify(service, times(2)).findById(anyString());
    }

    @Test
    public void deleteByIdTest(){
        when(service.deleteById("test")).thenReturn(true);
        when(service.deleteById("no")).thenReturn(false);

        //ticket exists
        ResponseEntity testResponse=controller.deleteById("test");
        assertEquals(HttpStatus.OK,testResponse.getStatusCode());

        //ticket doesn't exist
        ResponseEntity<Ticket> testResponseNotFound = controller.deleteById("no");
        assertEquals(HttpStatus.NOT_FOUND, testResponseNotFound.getStatusCode());

        verify(service, times(2)).deleteById(anyString());
    }

    @Test
    public void updateTicketTest() {
        Ticket updatedTicket = new Ticket("test", "test ticket updated", false);
        when(service.findById(eq("test"))).thenReturn(testTicket);
        when(service.update(anyString(), any())).thenReturn(updatedTicket);

        // success
        ResponseEntity<Ticket> testResponseSuccess = controller.updateTicket(updatedTicket, "test");
        assertEquals(HttpStatus.OK, testResponseSuccess.getStatusCode());
        assertEquals("test", testResponseSuccess.getBody().getId());
        assertEquals("test ticket updated", testResponseSuccess.getBody().getDescription());
        assertFalse(testResponseSuccess.getBody().isOpen());

        // failure due to no ticket to update
        ResponseEntity<Ticket> testResponseFail = controller.updateTicket(updatedTicket, "fail");
        assertEquals(HttpStatus.NOT_FOUND, testResponseFail.getStatusCode());
        assertNull(testResponseFail.getBody());

        verify(service, times(2)).findById(anyString());
        verify(service, times(1)).update(anyString(), any());
    }
}
