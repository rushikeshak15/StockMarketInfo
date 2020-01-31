# Stock Market Information Using RMI

PROBLEM STATEMENT – Design, implement, and thoroughly test a
distributed system using remote method invocation.

OBJECTIVE – To develop a distributed system for updating prices of
various stocks and notifying their subscribers of these updates.

THEORY -
Distributed System -
A distributed system is a network that consists of autonomous computers
that are connected using a distribution middleware. They help in sharing
different resources and capabilities to provide users with a single and
integrated coherent network.

a)Remote Procedure Call -
Remote Procedure Call (RPC) is a protocol that one program can use to
request a service from a program located in another computer on a network
without having to understand the network's details.
Like a regular or local procedure call, an RPC is a synchronous operation
requiring the requesting program to be suspended until the results of the
remote procedure are returned. However, the use of lightweight processes
or threads that share the same address space allows multiple RPCs to be
performed concurrently.

b)Remote Method Invocation -
Remote Method Invocation (RMI) is an API which allows an object to
invoke a method on an object that exists in another address space, which
could be on the same machine or on a remote machine. Through RMI,
object running in a JVM present on a computer (Client side) can invoke
methods on an object present in another JVM (Server side). RMI creates a
public remote server object that enables client and server side
communications through simple method calls on the server object.

WORKING OF MINI-PROJECT -
1)Interface
StockRMI – Interface for StockRMIServant
Notifiable – Interface for ClientUserGUI

2)Constant
AddressConstants – Stores values of IP address and Port

3)Server
StockRMIServer – Creates an object of StockRMIServant and
places it in RMI registery. Binds the service to an IP address and a port.
StockRMIServant – Implements StockRMI. Contains business logic of
the system. Maintains a hash map stocks to store stock information and a
hash map called users to store registered user clients. Implements
functions subscribe(), unSubscribe(), stockUpdate(), registerCallback(),
deRegisterCallback(), and doCallback().

4)ClientUser
ClientUserGUI – Implements Notifiable. It is a client who wishes to
subscribe to or unsubscribe from a particular stock. Whenever there is a
change in the stock price of a subscribed stock, this user is notified of the
new values.

5)ClientCompany
ClientPriceUpdateGUI – It is the client who wishes to update the
price of a stock. Objects of this class can call the stockUpdate() function of
the StockRMIServant object with two parameters: company name and
stock price. The updates are performed in the hash map called stocks on
server side.
