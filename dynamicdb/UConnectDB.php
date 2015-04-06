<?php
	class UConnectDB extends MyDatabase
	{
		private $link = null;
		
		public function __construct($login, $password, $databse, $hostname)
		{
			parent::__construct($login, $password, $databse, $hostname);
			$this->connect();
			$this->selectDatabase();
		}
		public function connect()
		{
			if(! is_null($this->link))
			{
				return false;
			}
			$link = @mysql_connect($this->hostname, $this->login, $this->password);
			if(! $link)
			{
                return false;
            }
            return true;
		}
		public function selectDatabase()
		{
			$ret = @mysql_select_db($this->database, $this->link);
			if(! $ret)
			{
                return false;
            }
            return true;
        }
        public function disconnect()
        {
            if($this->link)
            {
                if(@mysql_close())
                {
                    $this->link = false;
                    return true;
                }
                else
                {
                    return false;
                }
            }
        }
	}
?>
