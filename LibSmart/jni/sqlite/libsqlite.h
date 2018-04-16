/**
 * Copyrights:
 *  Netlogic Belgium:
 *   Ruben De Smet
 *
 * this file is licensed under the terms stated in the LICENCE file
 *
 */

#include <sqlite3.h>
#include <string>
#include <vector>
#include <exception>
#include <memory>
#include "character_tokenizer.h"

#ifndef SQLITEPP
#define SQLITEPP

namespace sqlite
{
    class sqlite;
    class statement;
    typedef std::shared_ptr<statement> statement_ptr;
    typedef std::shared_ptr<sqlite> sqlite_ptr;
    
    class exception : public std::exception
    {
        friend class statement;
        friend class sqlite;
    public:
        exception(std::string msg)
        {
            this->_msg = msg;
        }
        std::string what()
        {
            return "Sqlite error: " + this->_msg;
        }

        exception() throw() {

        };
        ~exception() throw() {

        };
    private:
        void _set_errorno(int err)
        {
        }
        std::string _msg;
    };

    class statement
    {
        friend class sqlite;
        friend class exception;
    public:
        void set_sql(std::string sql)
        {
            if(this->_prepared)
            {
                exception e("Can not set sql on prepared query.");
                throw e;
            }
            else
            {
                this->_sql = sql;
            }
        }
        void prepare()
        {
            this->_prepared = true;
            const char* tail;
            int rc = sqlite3_prepare_v2(this->_db, 
                    this->_sql.c_str(), 
                    this->_sql.length(), 
                    &this->_s, 
                    &tail);
            if(rc != SQLITE_OK)
            {
                exception e("Could not prepare sql.");
                e._set_errorno(rc);
                throw e;
            }
            this->_tail = std::string(tail);
        }

        bool step()
        {
            if(!this->_valid)
            {
                exception e("Trying to step an invalid statement.");
            }

            int rc = sqlite3_step(this->_s);
            if(rc == SQLITE_DONE)
            {
                this->_valid = false;
                return false;
            }
            if(rc == SQLITE_ROW)
            {
                this->_has_row = true;
                return true;
            }
            // Ok, this means error if we get here
            exception e("Sqlite had an error: " + std::string(sqlite3_errmsg(this->_db)));
            return false;
        }
        void reset()
        {
            int rc = sqlite3_reset(this->_s);
            if(rc != SQLITE_OK)
            {
                exception e("Could not reset the virtual machine.");
                throw e;
            }
            this->_valid = true;
            this->_has_row = false;
            this->_prepared = false;
        }

        void exec()
        {
            this->prepare();
            this->step();
        }

        double get_double(int fieldnumber)
        {
            return sqlite3_column_double(this->_s, fieldnumber);
        }

        int get_int(int fieldnumber)
        {
            return sqlite3_column_int(this->_s, fieldnumber);
        }

        std::string get_text(int fieldnumber)
        {
            return std::string((const char*)sqlite3_column_text(this->_s, fieldnumber));
        }
        std::string get_blob(int fieldnumber)
        {
            return std::string((const char*)sqlite3_column_blob(this->_s, fieldnumber), 
                    sqlite3_column_bytes(this->_s, fieldnumber));
        }

        void bind(int where, std::string text)
        {
            int rc = sqlite3_bind_text(this->_s, where, text.c_str(), text.length(), SQLITE_STATIC);
            if(rc != SQLITE_OK)
            {
                exception e("Could not bind text.");
                throw e;
            }
        }
        void bind(int where, double d)
        {
            int rc = sqlite3_bind_double(this->_s, where, d);
            if(rc != SQLITE_OK)
            {
                exception e("Could not bind double.");
                throw e;
            }
        }
        void bind(int where, int i)
        {
            int rc = sqlite3_bind_int(this->_s, where, i);
            if(rc != SQLITE_OK)
            {
                exception e("Could not bind int.");
                throw e;
            }
        }
        void bind_null(int where)
        {
            int rc = sqlite3_bind_null(this->_s, where);
            if(rc != SQLITE_OK)
            {
                exception e("Could not bind to NULL.");
                throw e;
            }
        }

        virtual ~statement()
        {
            sqlite3_finalize(this->_s);
        }
    private:
        statement(sqlite3* db)
        {
        	_s = NULL;
            this->_db = db;
            this->_prepared = false;
            this->_valid = true;
            this->_has_row = false;
        }
        statement(sqlite3* db, std::string sql)
        {
        	_s = NULL;
            this->_db = db;
            this->_prepared = false;
            this->_valid = true;
            this->_has_row = false;
            this->set_sql(sql);
        }
        sqlite3* _db;
        bool _prepared, _valid, _has_row;
        std::string _sql;
        std::string _tail;
        sqlite3_stmt* _s;
    };

    class sqlite
    {
        friend class statement;
    public:
        sqlite(std::string filename) throw (exception)
        {
            this->_filename = filename;
            int rc = sqlite3_open(filename.c_str(), &this->_db);
            if(rc != SQLITE_OK)
            {
                exception e("Could not open '" + filename + "'");
                throw e;
            }
            
//            const sqlite3_tokenizer_module *ptr;
//            char token_name[] = "porter";
//
//            // get the tokenizer
//            get_character_tokenizer_module(&ptr);
//
//            // register character tokenizer, note that you need to register it everytime the database is opened
//            registerTokenizer(_db, token_name, ptr);
        }
        std::shared_ptr<statement> get_statement()
        {
            statement_ptr st(new statement(this->_db));
            return st;
        }
        statement_ptr get_statement(std::string sql)
        {
            statement_ptr st(new statement(this->_db, sql));
            return st;
        }
        int64_t last_insert_id()
        {
            return sqlite3_last_insert_rowid(this->_db);
        }
        virtual ~sqlite()
        {
            sqlite3_close(this->_db);
        }
        
        void beginTransaction()
        {
            statement_ptr st(new statement(this->_db, "begin exclusive transaction"));
            st->exec();
        }
        
        void beginDifferedTransaction()
        {
            statement_ptr st(new statement(this->_db, "begin deferred transaction"));
            st->exec();
        }
        
        void rollback()
        {
            statement_ptr st(new statement(this->_db, "rollback transaction"));
            st->exec();
        }
        
        void commit()
        {
            statement_ptr st(new statement(this->_db, "commit transaction"));
            st->exec();
        }
        
        void executeUpdate(std::string sql)
        {
            statement_ptr st(new statement(this->_db, sql));
            st->exec();
        }
        
        sqlite3 * sqliteHandle()
        {
            return _db;
        }

    private:
        std::string _filename;

        sqlite3* _db;
        
        /*
         ** Register a tokenizer implementation with FTS3 or FTS4.
         */
        static int registerTokenizer(
                                     sqlite3 *db,
                                     char *zName,
                                     const sqlite3_tokenizer_module *p
                                     ){
            int rc;
            sqlite3_stmt *pStmt;
            const char *zSql = "SELECT fts3_tokenizer(?, ?)";
            
            rc = sqlite3_prepare_v2(db, zSql, -1, &pStmt, 0);
            if( rc!=SQLITE_OK ){
                return rc;
            }
            
            sqlite3_bind_text(pStmt, 1, zName, -1, SQLITE_STATIC);
            sqlite3_bind_blob(pStmt, 2, &p, sizeof(p), SQLITE_STATIC);
            sqlite3_step(pStmt);
            
            return sqlite3_finalize(pStmt);
        }
    };
}

#endif
