package bugTracker.controller;

import bugTracker.model.Ticket;
import bugTracker.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/tickets")
public class TicketController {

    private TicketService ticketService;


    @Autowired
    public void setTicketService(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity createTicket(@RequestBody Ticket ticket) {
        if (ticket.getDescription() == null) return ResponseEntity.badRequest().build();
        Ticket created = ticketService.create(ticket);
        if (created.getId() == null) return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
    public ResponseEntity getTicketById(@PathVariable String id) {
        Ticket found = ticketService.findById(id);
        if (found == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        return ResponseEntity.ok(found);
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
    public ResponseEntity updateTicket(@RequestBody Ticket ticket, @PathVariable String id) {
        Ticket found = ticketService.findById(id);
        if (found == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        Ticket updated = ticketService.update(id, ticket);
        return ResponseEntity.ok(updated);
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteById(@PathVariable String id) {
        boolean deleted = ticketService.deleteById(id);
        if (deleted) return ResponseEntity.ok().build();
        return ResponseEntity.notFound().build();
    }
}
