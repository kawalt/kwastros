package scoring;

public class Sub
{
    public Sub(int inning, String playerFirstName, String playerLastName, String subFirstName, String subLastName, int subID) {
        _inning = inning;
        _playerFirstName = playerFirstName;
        _playerLastName = playerLastName;
        _subFirstName = subFirstName;
        _subLastName = subLastName;
        _subID = subID;
    }
    
    public int getInning()
    {
        return _inning;
    }
    public void setInning(int _inning)
    {
        this._inning = _inning;
    }
    public String getPlayerFirstName()
    {
        return _playerFirstName;
    }
    public void setPlayerFirstName(String firstName)
    {
        _playerFirstName = firstName;
    }
    public String getPlayerLastName()
    {
        return _playerLastName;
    }
    public void setPlayerLastName(String lastName)
    {
        _playerLastName = lastName;
    }
    public String getSubFirstName()
    {
        return _subFirstName;
    }
    public void setSubFirstName(String firstName)
    {
        _subFirstName = firstName;
    }
    public String getSubLastName()
    {
        return _subLastName;
    }
    public void setSubLastName(String lastName)
    {
        _subLastName = lastName;
    }
    public int getSubID()
    {
        return _subID;
    }
    public void setSubID(int subID) {
        _subID = subID;
    }

    private int _inning;
    private String _playerFirstName;
    private String _playerLastName;
    private String _subFirstName;
    private String _subLastName;
    private int _subID;
}
