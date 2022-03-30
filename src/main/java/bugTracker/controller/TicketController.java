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

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Ticket> createTicket(@RequestBody Ticket ticket){
        if(ticket.getDescription()==null)return ResponseEntity.badRequest().body(null);
        Ticket created = ticketService.create(ticket);
        if(created.getId()==null) return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @RequestMapping(value= "/{id}",method = RequestMethod.GET)
    public ResponseEntity<Ticket> getTicket(@PathVariable String id){
        Ticket found=ticketService.get(id);
        if(found==null)return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        return ResponseEntity.ok(found);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Ticket> updateTicket(@RequestBody Ticket ticket, @PathVariable String id){
        Ticket found=ticketService.get(id);
        if(found==null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        Ticket updated=ticketService.update(id,ticket);
        return ResponseEntity.ok(updated);
    }
}
