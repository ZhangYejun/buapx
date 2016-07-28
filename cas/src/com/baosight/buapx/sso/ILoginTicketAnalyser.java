package com.baosight.buapx.sso;


 
public interface ILoginTicketAnalyser
{

   // public abstract TicketDefinition decode(String ticket);

    public abstract String encode(String username);

   // public abstract boolean validate(TicketDefinition ticketdefinition, String username);

    public abstract boolean validate(String ticket, String username);
}