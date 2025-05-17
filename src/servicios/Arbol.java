package servicios;

public class Arbol {
    private Nodo raiz;
    private int criterio;

    public Arbol() {
        raiz = null;
    }

    public int getCriterio() {
        return criterio;
    }

    public void setCriterio(int criterio) {
        this.criterio = criterio;
    }

    public void agregar(Nodo nodo) {
        agregar(raiz, nodo);
    }

    private void agregar(Nodo actual, Nodo nodo) {
        if (actual == null) {
            raiz = nodo;
        } else {
            // no insertar si el documento es igual
            if (nodo.getDocumento().equals(actual.getDocumento())) {
                return;
            } else if (ServicioDocumento.esMayor(nodo.getDocumento(), actual.getDocumento(), criterio)) {
                // agregar a la derecha
                if (actual.derecha == null) {
                    actual.derecha = nodo;
                } else {
                    agregar(actual.derecha, nodo);
                }
            } else {
                // agregar a la izquierda
                if (actual.izquierda == null) {
                    actual.izquierda = nodo;
                } else {
                    agregar(actual.izquierda, nodo);
                }
            }
        }
    }

    public void recorrerInOrden() {
        recorrerInOrden(raiz);
    }

    private void recorrerInOrden(Nodo nodo) {
        if (nodo != null) {
            recorrerInOrden(nodo.izquierda);
            System.out.println(nodo.getDocumento().getNombreCompleto() + " - " + nodo.getDocumento().getDocumento());

            recorrerInOrden(nodo.derecha);
        }
    }

}
