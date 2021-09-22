# How to run #

1) Compile the code.
2) Run `rmiregistry` from the directory containing the compiled Java packages
   (directories with `.class` files).
   This should be the root of the package hierarchy.
3) Run the `Server` program to register the server in the RMI registry.
4) Run the `Client` program to make a sample call to the server.