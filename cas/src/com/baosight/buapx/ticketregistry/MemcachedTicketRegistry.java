package com.baosight.buapx.ticketregistry;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeoutException;

import net.rubyeye.xmemcached.KeyIterator;
import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.exception.MemcachedException;

import org.jasig.cas.ticket.Ticket;
import org.jasig.cas.ticket.registry.AbstractTicketRegistry;
import org.springframework.util.Assert;

/**
 * Implementation of the TicketRegistry that is backed by  Memcached.
 * 
 * @author lizidi@baosight.com 2012.10.18
 */
public final class MemcachedTicketRegistry extends AbstractTicketRegistry{
	MemcachedClient client;
	private int ttl;
	private long timeout;

	
	
    public MemcachedTicketRegistry(MemcachedClient client,long timeout,int ttl){
		this.client=client;
		this.timeout=timeout;
		this.ttl=ttl;
    }
    
    
    
    /**
     * @throws IllegalArgumentException if the Ticket is null.
     */
    public void addTicket(final Ticket ticket) {
        Assert.notNull(ticket, "ticket cannot be null");
        if (log.isDebugEnabled()) {
            log.debug("Added ticket [" + ticket.getId() + "] to registry.");
        }
       
        
        try {
			client.set(ticket.getId(), this.ttl, ticket);
			if (log.isDebugEnabled())  
			 log.debug("Added ticket [" + ticket.getId() + "] to registry ok.");
		} catch (TimeoutException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (MemcachedException e) {
			e.printStackTrace();
		}
 
    }

    public Ticket getTicket(final String ticketId) {
        if (ticketId == null) {
            return null;
        }

        if (log.isDebugEnabled()) {
            log.debug("Attempting to retrieve ticket [" + ticketId + "]");
        }
        
       
        Ticket ticket=null;
        
        try {
			ticket=client.get(ticketId, this.timeout);
		} catch (TimeoutException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (MemcachedException e) {
			e.printStackTrace();
		}
        if (ticket != null) {
            log.debug("Ticket [" + ticketId + "] found in registry.");
        }else {
        	log.debug("Ticket [" + ticketId + "] not found in registry.");
        }

        return ticket;
    }

    public boolean deleteTicket(final String ticketId) {
        if (ticketId == null) {
            return false;
        }
        if (log.isDebugEnabled()) {
            log.debug("Removing ticket [" + ticketId + "] from registry");
        }
        
       
        boolean result=false;
        
        try {
			result=client.delete(ticketId, this.timeout);
		} catch (TimeoutException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (MemcachedException e) {
			e.printStackTrace();
		}
		
        return result;
    }

    public Collection<Ticket> getTickets() {
    	throw new UnsupportedOperationException("GetTickets not supported.");
    }

	
    
    
	public static void main(String args[]){
		 
		MemcachedTicketRegistry reg = new MemcachedTicketRegistry(null,1000,1000);
		reg.test();

	}
	
	public void test(){
		InetSocketAddress add= new InetSocketAddress("10.46.20.58",11211);
		//InetSocketAddress add1= new InetSocketAddress("10.46.20.58",11211);
		List<InetSocketAddress> list = new ArrayList<InetSocketAddress>();
		list.add(add);
		//list.add(add1);
		XMemcachedClientBuilder b= new XMemcachedClientBuilder(list);
		MemcachedClient xclient= null;
		try {
			xclient = b.build();
		} catch (IOException e1) {
			 
			e1.printStackTrace();
		}
		String key ="1TGT-1-aGydQkDEHixg9b4aFPfcrijqLLTWQ3AIFzc4Kc6RFQhITOsPLk-buapx_cas";
		 try {
			xclient.set(key, 1000000, key);
			
			 String getkey =xclient.get(key,8000);
			 //取所有的key
			 KeyIterator ite = xclient.getKeyIterator(add);
			  while (ite.hasNext()){
				  
				 String str =  ite.next();
				 System.out.println("loop key="+str);
			  }
			 System.out.println("key="+getkey);
		} catch (TimeoutException e) {
			 
			e.printStackTrace();
		} catch (InterruptedException e) {
			 
			e.printStackTrace();
		} catch (MemcachedException e) {
			 
			e.printStackTrace();
		}
		
	}
	
 
}
