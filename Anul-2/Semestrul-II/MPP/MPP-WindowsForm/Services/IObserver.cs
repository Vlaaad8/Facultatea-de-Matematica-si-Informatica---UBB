using DefaultNamespace;
using MPP_CSharpProject.domain;

namespace Services;

public interface IObserver
{
    void NewTicketBought(Flight flight);
}