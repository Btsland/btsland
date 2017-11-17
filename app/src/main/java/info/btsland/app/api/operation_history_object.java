package info.btsland.app.api;



public class operation_history_object {
    public String id;
    public operations.operation_type op;
    public int block_num;
    public int trx_in_block;
    public int op_in_trx;
    public int virtual_op;

    @Override
    public String toString() {
        return "operation_history_object{" +
                "id='" + id + '\'' +
                ", op=" + op +
                ", block_num=" + block_num +
                ", trx_in_block=" + trx_in_block +
                ", op_in_trx=" + op_in_trx +
                ", virtual_op=" + virtual_op +
                '}';
    }
}
