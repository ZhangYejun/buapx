package com.baosight.buapx.ticketregistry;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.jasig.cas.ticket.Ticket;
import org.jasig.cas.ticket.TicketGrantingTicketImpl;
import org.jasig.cas.ticket.registry.AbstractTicketRegistry;
import org.springframework.util.Assert;

import com.baosight.buapx.redis.JedisTemplate;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;

/**
 * Implementation of the TicketRegistry that is backed by a Redis.
 *
 * @author lizidi@baosight.com 2012.6.3
 */
public final class RedisTicketRegistry extends AbstractTicketRegistry  {
	private JedisTemplate jedisTemplate;
	private int ttl;



    public RedisTicketRegistry(JedisPoolConfig poolConfig,String host,int port,int timeout,int ttl, String password){
    	JedisPool pool = new JedisPool(poolConfig, host, port, timeout, password);
    	this.jedisTemplate=new JedisTemplate(pool);
		this.ttl=ttl;
    }

    public RedisTicketRegistry(JedisPoolConfig poolConfig,String host,int port,int timeout,int ttl){
    	JedisPool pool = new JedisPool(poolConfig, host, port, timeout);
    	this.jedisTemplate=new JedisTemplate(pool);
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

        ByteArrayOutputStream byteOs=new ByteArrayOutputStream();
        ObjectOutputStream objectOs=null;
        try {
			objectOs=new ObjectOutputStream(byteOs);
			objectOs.writeObject(ticket);
			byte[] b=byteOs.toByteArray();
			jedisTemplate.set(ticket.getId().getBytes(), b);
			jedisTemplate.expire(ticket.getId().getBytes(), ttl);

		}catch(IOException e){
			e.printStackTrace();
		}finally{
			try {
				if(byteOs!=null){
				   byteOs.close();
				}
				if(objectOs!=null){
					objectOs.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}



    }

    public Ticket getTicket(final String ticketId) {
        if (ticketId == null) {
            return null;
        }

        if (log.isDebugEnabled()) {
            log.debug("Attempting to retrieve ticket [" + ticketId + "]");
        }

        ByteArrayInputStream byteIs;
        byte[] byteTicket=jedisTemplate.get(ticketId.getBytes());
        Ticket ticket=null;
        if(byteTicket!=null){
        	byteIs=new ByteArrayInputStream(byteTicket);
    		ObjectInputStream objectIs=null;

    		try {
    			objectIs = new ObjectInputStream(byteIs);
    			ticket = (Ticket) objectIs.readObject();
    		} catch (Exception e){
    			e.printStackTrace();
    		}finally{
    				try {
    					if(byteIs!=null){
						   byteIs.close();
    					}
    					if(objectIs!=null){
    						objectIs.close();
    					}
					} catch (IOException e) {
						e.printStackTrace();
					}

    		}
        }
        if (ticket != null) {
            log.debug("Ticket [" + ticketId + "] found in registry.");
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

        long result=0;
        try{
        	jedisTemplate.del(ticketId.getBytes());
        }catch(Exception e){
        	e.printStackTrace();
        }finally{
        }

        return true;
    }

    public Collection<Ticket> getTickets() {
    	throw new UnsupportedOperationException("GetTickets not supported.");
    }





}
