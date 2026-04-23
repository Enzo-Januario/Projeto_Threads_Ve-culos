package common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ILoja extends Remote {

    String PREFIXO_SERVICO_RMI = "Loja_";

    Veiculo comprarVeiculo(int idCliente) throws RemoteException;

    int getId() throws RemoteException;

    boolean estaAtiva() throws RemoteException;
}
