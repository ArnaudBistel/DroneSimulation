/*********************************************
 * OPL 20.1.0.0 Model
 * Author: Mael
 * Creation Date: 18 déc. 2021 at 07:55:28
 *********************************************/

 /*****************************************************************************
 *
 * DATA
 * 
 *****************************************************************************/
using CP;


// Clients
int     n       = ...;
range   Clients  = 1..n;
range	Nodes = 0..n;
int poids[Nodes] = ...;

// Edges -- sparse set
tuple       edge        {int i; int j;}
setof(edge) Edges       = {<i,j> | ordered i,j in Nodes};
int         dist[Edges] = ...;

// Decision variables
dvar int succ[Clients];
dvar int pred[Clients];


float energy_cap = 1628.0;

/*****************************************************************************
 *
 * MODEL
 * 
 *****************************************************************************/

// Objective
minimize sum (i in Clients) (pred[i] == 0) * dist[<0,i>] * energy_cap/(100-5*(poids[i] + poids[succ[i]] + poids[succ[succ[i]]])) + 
		sum (i in Clients) (succ[i] != 0) * dist[<minl(i, succ[i]), maxl(i, succ[i])>] * energy_cap/(100-5*( poids[succ[i]] + poids[succ[succ[i]]])) + 
		sum (i in Clients) (succ[i] == 0) * dist[<0,i>] * energy_cap/110;
subject to {
   
   // Each city is linked with two other cities
  
          
};

execute {
  writeln("Test");
          
  for(var c in Clients) {  
      writeln("Client " + t + ": Pred=" + pred[c] + " Succ= " + succ[c]);
    }
};
 