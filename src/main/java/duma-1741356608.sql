CREATE TABLE [Duma] (
	[id] int IDENTITY(1,1) NOT NULL UNIQUE,
	[guid] nvarchar(max) NOT NULL,
	[date] date NOT NULL,
	[macAdress] nvarchar(max) NOT NULL,
	[ipAdress] nvarchar(max) NOT NULL,
	[port] int NOT NULL,
	[status] nvarchar(max) NOT NULL,
	[number] int NOT NULL,
	PRIMARY KEY ([id])
);

CREATE TABLE [Parameter] (
	[id] int IDENTITY(1,1) NOT NULL UNIQUE,
	[datetime] date NOT NULL,
	[name] nvarchar(max) NOT NULL,
	[codParameter] nvarchar(max) NOT NULL,
	[lastUpdate] nvarchar(max) NOT NULL,
	[meaning] int NOT NULL,
	[dumaId] int NOT NULL,
	PRIMARY KEY ([id])
);

CREATE TABLE [DataFile] (
	[id] int IDENTITY(1,1) NOT NULL UNIQUE,
	[nameFile] nvarchar(max) NOT NULL,
	[dateTime] date NOT NULL,
	[filesize] int NOT NULL,
	[data[]] varbinary(max) NOT NULL,
	[extension] nvarchar(max) NOT NULL,
	[indexFile] int NOT NULL,
	[idNumberFolder] int NOT NULL,
	[headerSize] int NOT NULL,
	PRIMARY KEY ([id])
);

ALTER TABLE [Duma] ADD CONSTRAINT [Duma_fk0] FOREIGN KEY ([id]) REFERENCES [Parameter]([dumaId]);

