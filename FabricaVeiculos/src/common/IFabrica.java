package common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IFabrica extends Remote {

    String NOME_SERVICO_RMI = "FabricaVeiculos";

    Veiculo comprarVeiculo(int idLoja) throws RemoteException;

    boolean estaAtiva() throws RemoteException;
}
