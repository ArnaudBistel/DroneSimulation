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
//using CP;


/*// Clients
int     n       = ...;
range   Clients  = 1..n;
range	Nodes = 0..n;
int poids[Nodes] = ...;

// Edges -- sparse set
tuple       edge        {int i; int j;}
setof(edge) Edges       = {<i,j> | ordered i,j in Nodes};
float         dist[Edges] = ...;*/

int n_client = ...;
int n_route = ...;
range Clients = 1..n_client;
range Routes = 1..n_route;

tuple Subtour{
  //int id;
  //int size;
  {int} nodes;
  float cost;
}

Subtour subtours[Routes] = ...;

int visited[Routes][Clients];

execute{
  for (var r in Routes){
    /*for (c in Clients){
      visited[r][c] = 0;*/
	  for(var e in subtours[r].nodes){
	    if (e != 0){
	    	visited[r][e] = 1;
	    }
	  }
      
    //}
  }
}

dvar boolean x[Routes];

minimize sum (r in Routes) x[r] * subtours[r].cost;

subject to {
  forall(c in Clients)
    sum (r in Routes) visited[r][c] * x[r] == 1;
}


/*
// Decision variables
dvar int succ[Nodes];
dvar int pred[Nodes];


int energy_cap = 1628;

/*****************************************************************************
 *
 * MODEL
 * 
 ***************************************************************************** /

// Objective
minimize sum (c in Clients) dist[<minl(c, succ[c]), maxl(c, succ[c])>] * energy_cap / (100 - 5 * poids[c]);
subject to {
   
   forall(c in Clients)
     (sum (j in Clients) pred[j] == c + sum (k in Clients) succ[k] == c + pred[c] == 0 + succ[c] == 0 ) == 2; 
     
   succ[0] == 0;
   pred[0] == 0;
   
     
     
     /*sum (i in Clients) (pred[i] == 0) * dist[<0,i>] * energy_cap/(100-5*(poids[i] + poids[succ[i]] + poids[succ[succ[i]]])) + 
		sum (i in Clients) (succ[i] != 0) * dist[<minl(i, succ[i]), maxl(i, succ[i])>] * energy_cap/(100-5*( poids[succ[i]] + poids[succ[succ[i]]])) + 
		sum (i in Clients) (succ[i] == 0) * dist[<0,i>] * energy_cap/110;
   //Each city is linked with two other cities
  
          
};*/

execute {
  writeln("Test");
          
  for(var r in Routes) {
    if (x[r] == 1 ){
      write("Route " + r + ": Nodes=( ");
      for (var e in subtours[r].nodes) {
        write(e + " ");
      }
      write(")")
      writeln();
    }  
  }      
};