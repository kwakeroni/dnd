package active.io.xml;

import java.util.Optional;
import java.util.function.Function;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.SingleValueConverter;

public class XStreamBuilder {
    
    private Optional<XStream> xstream = Optional.of(new XStream());
    
    public XStreamBuilder(){
        
    }
    
    public XStream build(){
        XStream result = xstream.orElseThrow(() -> new IllegalStateException("XStream already built"));
        this.xstream = Optional.empty();
        return result;
    }

    public <T> XStreamClassBuilder<T> with(Class<T> type){
        return new XStreamClassBuilder<T>(type, this.xstream.get());
    }
    
    public class XStreamClassBuilder<T> {
        private final Class<T> type;
        private final XStream xstream;
        
        private XStreamClassBuilder(Class<T> type, XStream xstream){
            this.type = type;
            this.xstream = xstream;
        }
        
        public XStreamClassBuilder<T> as(String alias){
            xstream.alias(alias, type);
            return this;
        }
        
        public XStreamClassBuilder<T> asValue(Function<? super T, String> extractor, Function<String, ? extends T> creator){
            xstream.addImmutableType(type);
            xstream.registerConverter(new SingleValueConverter() {
                
                @Override
                public boolean canConvert(Class arg0) {
                    return arg0 == type;
                }
                
                @Override
                public String toString(Object arg0) {
                    return extractor.apply((T) arg0);
                }
                
                @Override
                public Object fromString(String arg0) {
                    return creator.apply(arg0);
                }
            });
            return this;
        }
        
        public <S extends T> XStreamClassBuilder<T> implementedBy(Class<S> defaultImpl){
            xstream.addDefaultImplementation(defaultImpl, type);
            return this;
        }
        
        public XStreamClassBuilder<T> withAttributes(String... attributes){
            for( String att : attributes){
                xstream.useAttributeFor(type, att);
            }
            return this;
        }
        
        public XStreamClassBuilder<T> withImplicit(String... collections){
            for (String coll : collections){
                xstream.addImplicitCollection(type, coll);
            }
            return this;
        }
        
        public XStreamClassBuilder<T> using(ConverterBuilder<T> builder){
            return using(builder.build(this.type));
        }
        
        public XStreamClassBuilder<T> using(Converter converter){
            xstream.registerConverter(converter);
            return this;
        }
        
        
        public <U> XStreamClassBuilder<U> and(Class<U> type){
            return XStreamBuilder.this.with(type);
        }
        
        public XStream build(){
            return XStreamBuilder.this.build();
        }
    }

}
